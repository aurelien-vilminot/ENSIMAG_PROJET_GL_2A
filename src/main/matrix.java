class matrix{
    ///// Vector part /////
    float[] plus(float[] vector1, float[] vector2){
        if(vector1.length == vector2.length) {
            float[] vec = float[vector1.length];
            int i = 0;
            while(i<vector1.length){
                vec[i] = vector1[i] + vector2[i];
                i = i + 1;
            }
            return vec;
        } else {
            // Throw an error ?
        }
    }


    float[] minus(float[] vector1, float[] vector2){
        if(vector1.length == vector2.length) {
            float[] vec = float[vector1.length];
            int i = 0;
            while(i<vector1.length){
                vec[i] = vector1[i] - vector2[i];
                i = i + 1;
            }
            return vec;
        } else {
            // Throw an error ?
        }
    }

    float mult(float[] vector1, float[] vector2){
        if(vector1.length == vector2.length) {
            float result;
            int i = 0;
            while(i<vector1.length){
                result = result + vector1[i] * vector2[i];
                i = i + 1;
            }
            return result;
        } else {
            // Throw an error ?
        }
    }

    float[] linspace(float min, float max, float step){
        int len = (int) ((max - min) / step), i = 0;
        float[] vec = new float[len];
        while(i < len){
            vec[i] = min + step * i;
            i = i + 1;
        }
        return vec;
    }

    ///// Matrix part /////

    /*
    Returns a matrix filled with elem at each component.
    The size of the matrix is height x width.
     */
    float[][] uniform(int height, int width, float elem){
        float[][] mat = new float[height][width];
        i = 0;
        while(i < height){
            j = 0;
            while(j < width){
                mat[i][j] = elem;
                j = j + 1;
            }
            i = i + 1;
        }
        return mat;
    }

    /*
    Reshapes a vector in matrix of height * width size.
    The length of vector parameter must be equals to height * width.
     */
    float[][] reshape(int height, int width, float[] vector){
        if(vector.length != height * width){
            // return an error ?
        }
        float[][] mat = new float[height][width];
        int i = 0;
        while(i < height){
            int j = 0;
            while(j < width){
                mat[i][j] = vector[i * height + j];
                j = j + 1;
            }
            i = i + 1;
        }
        return mat;
    }






}

