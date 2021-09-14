package Second;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kohonen {
    public static List<Neuron> layer = new ArrayList<>();

    public static double learningRate = 1;            //скорость обучения
    public static double radius = 1;                  //радиус соседской функции
    public static double decreasingValue = 0.5;       //константа, используемая для изменения значений радиуса и скорости обучения
    public static double minPotential = 0.01;         //минимальный потенциал

    public void learn(double[] inputs, String value) {
        inputs = normalize(inputs);                    //нормализация исходных данных
        for (int i = 0; i < layer.size(); i++) {       //передача нормализированных данных нейронам
            layer.get(i).setInputs(inputs);
        }

        double[] distance = calculateDistance();        //считаем дистанцию от входного вектора до всех нейронов

        double minDistance = distance[0];      //предпалагает, что побкдитель - нулевой
        int neuronWinner = 0;
        for (int i = 1; i < distance.length; i++) {   //идем по всем, кроме нулевого
            if (layer.get(i).getPotential() < minPotential)     //если потенциал меньше минимального, то не учавствует
                continue;

            if (layer.get(0).getPotential() < minPotential && neuronWinner == 0) {
                minDistance = distance[i];  //если предпалагаем, что у нулевого потенциал тоже меньше минимального то делаем
                neuronWinner = i;           // победителем текущего
            }

            if (distance[i] < minDistance) {     //если у текущего нейрона дистанция меньше, то он победитель
                minDistance = distance[i];
                neuronWinner = i;
            }
        }

        for (int i = 0; i < 5; i++) {           //смещение нейрона-победителя
            double weight = layer.get(neuronWinner).getWeight(i);
            double input = layer.get(neuronWinner).getInput(i);
            double res = weight + learningRate * (input - weight);
            layer.get(neuronWinner).setWeight(i, res);
        }

        for (int i = 0; i < 16; i++) {     //проходимся по всей карте
            double X1 = layer.get(i).getX();  //позиция текущего
            double Y1 = layer.get(i).getY();
            double Xw = layer.get(neuronWinner).getX();  //позиция победителя
            double Yw = layer.get(neuronWinner).getY();
            if (X1 - Xw <= radius || X1 - Xw >= -radius || Y1 - Yw <= radius || Y1 - Yw >= -radius) { // если находится в радиусе
                double distanceForWinner = Math.pow((X1 - Xw), 2) + Math.pow((Y1 - Yw), 2);
                if (distanceForWinner == 0) {
                    layer.get(neuronWinner).setPotential(layer.get(neuronWinner).getPotential() - minPotential);  //у побелителя уменьшаем потенциал
                } else {
                    layer.get(i).setPotential(layer.get(i).getPotential() + 1 / 16);     //у остальных поднимаем потенциал

                    double beta = Math.exp(-distanceForWinner / (2 * radius * radius));   //доп значение при смещении соседа
                    for (int j = 0; j < 5; j++) {
                        double weight = layer.get(i).getWeight(j);                     //смещаем соседей
                        double input = layer.get(i).getInput(j);
                        double movedWeight = weight + learningRate * beta * (input - weight);
                        layer.get(i).setWeight(j, movedWeight);
                    }
                }
            }
        }
    }

    //находим расстояние от входного вектора до каждого нейрона
    public double[] calculateDistance() {
        double[] result = new double[16];
        double distance;
        for (int i = 0; i < 16; i++) { //проходимся по каждому нейрону
            distance = 0;
            for (int j = 0; j < 5; j++) {   //проходимся в нейроне по каждому параметру
                distance += Math.pow((layer.get(i).getInput(j) - layer.get(i).getWeight(j)), 2);
            }
            result[i] = Math.sqrt(distance);   //евклидово расстояние
        }
        return result;
    }

    //нормализация исходных данных элемента обучающего множества
    public double[] normalize(double[] inputs) {
        double divider = 0;
        for (int i = 0; i < inputs.length; i++) {
            divider += Math.pow(inputs[i], 2);
        }
        divider = Math.sqrt(divider);
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = inputs[i] / divider;
        }
        return inputs;
    }

    public void showMap(Map<String, double[]> data, int index) {
        final DecimalFormat decimalFormat = new DecimalFormat("0.000");  //чтобы отображалось нормально
        double[][] map = new double[4][4];   //формируем мапу
        double[] res = new double[16];

        for (String key : data.keySet()) {      //проходимся по обучающему множеству
            double[] inputs = data.get(key);   //гетаем параметры объекта
            for (int i = 0; i < 16; i++) {      //идем по нейронам
                double sum = 0;
                for (int j = 0; j < 9; j++) {
                    sum += Math.pow((inputs[j] - layer.get(i).getWeight(j)), 2);   //считаем у всех нейронов дистанцию для текущего элемента обучающего множества
                }
                res[i] = Math.sqrt(sum);
            }

            double minDistance = res[0];
            int neuronWinner = 0;
            for (int i = 0; i < res.length; i++) {
                if (res[i] < minDistance) {
                    minDistance = res[i];
                    neuronWinner = i;
                }
            }
            map[layer.get(neuronWinner).getX()][layer.get(neuronWinner).getY()] = inputs[index];   //отображаем на мапе победителя по выбранному параметру
        }


        for (int i = 0; i < 4; i++) {                                //отрисовка
            for (int j = 0; j < 4; j++) {
                System.out.print(decimalFormat.format(map[i][j]) + " ");
            }
            System.out.println();
        }
    }

    public void showMap1(Map<String, double[]> data, int index) {
        double[] res = new double[16];
        final DecimalFormat decimalFormat = new DecimalFormat("0.000");
        Map<Integer, List<double[]>> map = new HashMap<>();    //нейроны, хранящие входящие в них записи(для которых он победитель)
        for (int i = 0; i < 16; i++) {
            map.put(i, new ArrayList<double[]>());
        }
        for (String key : data.keySet()) {      //проходимся по обучающему множеству
            double[] inputs = data.get(key);   //гетаем параметры объекта
            for (int i = 0; i < 16; i++) {      //идем по нейронам
                double sum = 0;
                for (int j = 0; j < 5; j++) {
                    sum += Math.pow((inputs[j] - layer.get(i).getWeight(j)), 2);   //считаем у всех нейронов дистанцию
                }
                res[i] = Math.sqrt(sum);
            }

            double minDistance = res[0];
            int neuronWinner = 0;
            for (int i = 0; i < res.length; i++) {
                if (res[i] < minDistance) {
                    minDistance = res[i];
                    neuronWinner = i;
                }
            }
            map.get(neuronWinner).add(inputs);    //добавляем это наблюдение в победителя
        }

        for (int i = 0; i < 16; i++) {                                //отрисовка
            if (i != 0 && i % 4 == 0) {
                System.out.println();
            }
            double summ = 0;
            for (double[] dd : map.get(i)) {
                summ += dd[index];               //получаем сумму значений всех наблюдений этого нейрона
            }
            if (summ != 0) summ /= map.get(i).size();  //делим сумму на количество и получаем avg

            System.out.print(decimalFormat.format(summ) + " ");
        }
    }
}
