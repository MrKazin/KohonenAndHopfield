package Third;

public class Hopfield {
    int number = 0;
    int size;
    float[][] matrix;

    public Hopfield(int size) {
        this.size = size;
        matrix = new float[size][size];
        fillMatrix();
    }

    public void fillMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = 0;
            }
        }
        number = 0;
    }

    public void study(float[] inputArray) {
        if (inputArray.length != size) {
            System.out.println("Пример не соответсвует размерам");
            return;
        }
        inputArray = passThroughAF(inputArray);
        float[][] newMatrix = multiplicateSelfTransposedMatrix(inputArray);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] += newMatrix[i][j];
            }
        }
        number++;
    }

    private float[] passThroughAF(float[] inputArray) {
        for (int i = 0; i < inputArray.length; i++) {
            inputArray[i] = activationFunction(inputArray[i]);
        }
        return inputArray;
    }

    private float activationFunction(float inputFloat) {
        if (inputFloat > 0) return 1;
        else return -1;
    }

    private float[][] multiplicateSelfTransposedMatrix(float[] inputArray) {
        float[][] newMatrix = new float[size][size];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray.length; j++) {
                if (i == j)
                    newMatrix[i][j] = 0;
                else
                    newMatrix[i][j] = inputArray[i] * inputArray[j];
            }
        }
        return newMatrix;
    }

    public float[] recognizeForm(float[] inputArray) {
        if (number == 0) {
            System.out.println("Нет ни одного образа в памяти");
            return new float[size];
        }
        if (inputArray.length != size) {
            System.out.println("Пример не соответсвует размерам");
            return new float[size];
        }
        float[] answer = inputArray;
        float[] old_answer;
        int iteration = 0;
        do {
            old_answer = answer;
            iteration++;
            for (int i = 0; i < size; i++) {
                float[] matrixRow = matrix[i];
                float elem = 0;
                for (int j = 0; j < inputArray.length; j++) {
                    float m = matrixRow[j] / number;
                    elem += inputArray[j] * m;
                }
                answer[i] = activationFunction(elem);
            }
        }
        while (!equalArrays(inputArray, answer) && iteration < 100000);
        return answer;
    }

    private boolean equalArrays(float[] inputArray1, float[] inputArray2) {
        for (int i = 0; i < inputArray2.length; i++) {
            if (inputArray1[i] != inputArray2[i]) return false;
        }
        return true;
    }

    public void printMatrix(float[] inputArray, int rows,int columns){
        for (int i = 0; i < inputArray.length; i++) {
            if(i != 0 && i%columns == 0)System.out.println("");
            String toPrint = inputArray[i] == 1 ? "▓ " : "░ ";
            System.out.print(toPrint);
        }
        System.out.println("");
    }
}
