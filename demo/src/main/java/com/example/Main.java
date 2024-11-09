package com.example;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.Sensor;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        List<Sensor> sensorReadings = Arrays.asList(
                new Sensor("Sensor1", 22.0),
                new Sensor("Sensor2", 80.0),
                new Sensor("Sensor1", 95.0),
                new Sensor("Sensor3", 45.0)
        );

        Observable<Sensor> sensorStream = Observable.fromIterable(sensorReadings)
                //.concatWith(Observable.error(new RuntimeException("Sensor error detected")))
                .delay(1, TimeUnit.SECONDS, Schedulers.io()); // Simula lecturas cada segundo

        sensorStream
                .filter(sensor -> sensor.getTemperature() > 30 && sensor.getTemperature() < 90) // 1. Filtrar temperaturas seguras
                .map(sensor -> new Sensor(sensor.getId(), sensor.getTemperature() * 9 / 5 + 32))  // 2. Convertir de Celsius a Fahrenheit
                .flatMap(sensor -> Observable.just(sensor)                                     // 3. Multiplicar lecturas
                        .doOnNext(s -> System.out.println("Temperature reading: " + s.getTemperature() + "°F from " + s.getId())))
                .onErrorResumeNext(throwable -> {                                             // 4. Manejar errores
                    System.err.println("Error detected: " + throwable.getMessage());
                    return Observable.empty(); // Ignorar errores y continuar
                })
                .subscribe(
                        sensor -> System.out.println("Processed data: Sensor " + sensor.getId() + " - " + sensor.getTemperature() + "°F"),
                        throwable -> System.out.println("Final error handler: " + throwable.getMessage())
                );

        // Agregar una pausa para permitir que el flujo complete su ejecución
        Thread.sleep(5000); // Esperar 5 segundos para ver todos los resultados
    }
}

