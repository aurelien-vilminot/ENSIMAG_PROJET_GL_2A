class matrix{
    ///// Vector part /////
    float[] add(float[] vector1, float[] vector2){
        if(vector1.length != vector2.length){

        } else {

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

    float[][] ones(int height, int width){
        float[][] mat = new float[height][width];
        i = 0;
        while(i < height){
            j = 0;
            while(j < width){
                mat[height][width] = 1.0;
                j = j + 1;
            }
            i = i + 1;
        }
        return mat;
    }

}

