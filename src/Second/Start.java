package Second;

import java.util.HashMap;
import java.util.Map;

public class Start {
    public static void main(String[] args) {
        Kohonen kohonen = new Kohonen();
        Map<String, double[]> data = new HashMap<>();
        data.put("Ваз 2106", new double[]{4,4,1035,1370,39});
        data.put("Ваз 2110", new double[]{4,4,1360,1420,43});
        data.put("SL 300",   new double[]{2,4,1630,1317,55});
        data.put("Corolla",  new double[]{4,4,1400,1465,50});
        data.put("Нива",     new double[]{5,4,1400,1640,55});
        data.put("Ока",      new double[]{3,4,665,1300,30});
        data.put("Гранта",   new double[]{4,4,1160,1500,50});
        data.put("VW Tiguan",new double[]{5,4,1696,1630,60});
        data.put("BMW X5",   new double[]{5,4,2350,1745,80});
        data.put("octavia",  new double[]{4,4,1300,1480,50});

        data.put("Камаз 4308",new double[]{2,6,11990,3470,210});
        data.put("Зил 130",   new double[]{2,4,4900,2400,170});
        data.put("Аксор",     new double[]{2,6,22000,3467,550});
        data.put("Маз 544008",new double[]{2,6,18750,4000,500});
        data.put("Урал",      new double[]{2,6,8265,2715,330});
        data.put("Ман Ф90",   new double[]{2,6,40000,2650,700});
        data.put("Газ 53",    new double[]{2,4,7400,2190,90});
        data.put("BAW Fenix 1044",new double[]{2,4,3500,2220,90});
        data.put("Ford Transit",  new double[]{2,4,2000,1989,80});
        data.put("Iveco Stralis", new double[]{2,6,12330,2750,800});

        data.put("Molot ZL 20",new double[]{2,4,6000,3200,40});
        data.put("МКСМ 800",   new double[]{1,4,2800,2215,55});
        data.put("Краз 6510",  new double[]{2,6,24900,2830,165});
        data.put("МТЗ 80",     new double[]{2,4,6300,2785,130});
        data.put("Molot 300M", new double[]{2,4,6500,3050,64});
        data.put("Вектор 410", new double[]{1,8,11075,3475,540});
        data.put("Галичанин",  new double[]{2,4,20500,3915,210});
        data.put("Т 40",       new double[]{2,4,2300,2370,74});
        data.put("бкм - 317",  new double[]{2,4,5950,3500,100});
        data.put("New Holland B100B",new double[]{2,4,8350,2940,135});


        //создаем карту размерности 4 на 4
        for (int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                Neuron neuron = new Neuron();
                neuron.setX(i);
                neuron.setY(j);
                Kohonen.layer.add(neuron);
            }
        }

        for(int i = 0; i < 100; i++) {
            for (String key : data.keySet()) {
                kohonen.learn(data.get(key), key);  //передем элемент множества обучения в learn
            }
            kohonen.radius = kohonen.radius *Math.exp(-i/ kohonen.decreasingValue);
            kohonen.learningRate = kohonen.learningRate *Math.exp(-i/ kohonen.decreasingValue);
        }


        System.out.println("Масса");
        kohonen.showMap1(data, 2);
        System.out.println("\n");

        System.out.println("Просвет в мм");
        kohonen.showMap1(data, 3);
        System.out.println("\n");

        System.out.println("Объем бака");
        kohonen.showMap1(data, 4);
        System.out.println();
    }
}
