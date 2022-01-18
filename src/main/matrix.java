class matrix{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// MATRIX PART //////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////
    ///// Generate matrix and resizing /////
    ////////////////////////////////////////

    /*
    Returns a matrix filled with elem at each component.
    The size of the matrix is height x width.
     */
    float[][] uniformMat(int height, int width, float value){
        float[][] mat = new float[height][width];
        int i = 0;
        while(i < height){
            mat[i] = uniformVec(width, value);
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
            return null;
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

    ///////////////////////////////////
    //// Calcul with two matrices /////
    ///////////////////////////////////

    /*
    Returns true if mat1 is squarred. Otherwise returns false.
     */
    boolean isSquarred(float[][] mat){
        //TODO : est faux. Il faut vérifier que toutes les lignes aient le même nombre d'élements.
        return mat.length == mat[0].length;
    }


    /*
    Returns true if mat1 and mat2 have same dimension. Otherwise returns false.
    This method is used in all methods which need to verify if two matrices have same dimensions (add, sub...).
     */
    boolean sameDim(float[][] mat1, float[][] mat2){
        // How to treat the case when mat1 is void ?
        if(mat1.length != mat2.length) {
            return false;
        } else if(mat1[0] == null){
            return false;
        } else {
            int rowLength = mat1[0].length, i = 0;
            while(i < mat1.length){
                if((mat1[i].length != rowLength) || (mat1[i].length != mat2[i].length)){
                    return false;
                }
                i = i + 1;
            }
            return true;
        }
    }

    /*
    Returns a matrix corresponding to the sum of mat1 and mat2.
    If mat1 and mat2 don't have same dimensions, return null.
     */
    float[][] addMat(float[][] mat1, float[][] mat2){
        // Verify mat1 and mat2 have same dimensions.
        if(!sameDim(mat1, mat2)){
            return null;
        }

        // Sum each component of mat1 with each of mat2
        float [][] mat = float[mat1.length][mat1[0].length];
        int i = 0;
        while(i < mat1.length){
            int j = 0;
            while(j < mat1[0].length){
                mat[i][j] = mat1[i][j] + mat2[i][j];
                j = j + 1;
            }
            i = i + 1;
        }
        return mat;
    }

    /*
    Returns a matrix corresponding to the difference of mat1 and mat2.
    If mat1 and mat2 don't have same dimensions, return null.
     */
    float[][] subMat(float[][] mat1, float[][] mat2){
        // Verify mat1 and mat2 have same dimensions.
        if(!sameDim(mat1, mat2)){
            return null;
        }

        // Sub each component of mat1 with each of mat2
        float [][] mat = float[mat1.length][mat1[0].length];
        int i = 0;
        while(i < mat1.length){
            int j = 0;
            while(j < mat1[0].length){
                mat[i][j] = mat1[i][j] - mat2[i][j];
                j = j + 1;
            }
            i = i + 1;
        }
        return mat;
    }


    ////////////////////////////
    ///// Op on one matrix /////
    ////////////////////////////

    // Function to get cofactor of
    // mat[p][q] in temp[][]. n is
    // current dimension of mat[][]
    static void getCofactor(int mat[][], int temp[][],
                            int p, int q, int n)
    {
        int i = 0, j = 0;

        // Looping for each element of
        // the matrix
        for (int row = 0; row < n; row++)
        {
            for (int col = 0; col < n; col++)
            {
                // Copying into temporary matrix
                // only those element which are
                // not in given row and column
                if (row != p && col != q)
                {
                    temp[i][j++] = mat[row][col];
                    // Row is filled, so increase
                    // row index and reset col
                    // index
                    if (j == n - 1)
                    {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    /* Recursive function for finding determinant
    of matrix. n is current dimension of mat[][]. */
    float determinantMat(float[][] mat, int n)
    {
        // Base case : if matrix contains one single element
        if (n == 1){
            return mat[0][0];
        }

        // Initialize result
        float det = 0;

        // To store cofactors
        float[][] cofactor = new float[n][n];

        // To store sign multiplier
        int sign = 1;

        // Iterate for each element of first row
        for (int f = 0; f < n; f++)
        {
            // Getting Cofactor of mat[0][f]
            getCofactor(mat, temp, 0, f, n);
            D += sign * mat[0][f]
                    * determinantOfMatrix(temp, n - 1);

            // terms are to be added with
            // alternate sign
            sign = -sign;
        }

        return D;
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// VECTOR PART //////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    Returns true if vec1 and vec2 have same size. Otherwise return null.
     */
    boolean sameSize(float[] vec1, float[] vec2){
        return vec1.length == vec2.length;
    }

    /*
    Returns a vector of length len with each component equals to value.
     */
    float[] uniformVec(int len, float value){
        float[] vect = new float[len];
        int i = 0;
        while(i < len){
            vect[i] = value;
            i = i + 1;
        }
        return vect;
    }


    ///// Vector part /////
    float[] addVec(float[] vector1, float[] vector2){
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


    float[] subVec(float[] vector1, float[] vector2){
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

    float multVec(float[] vec1, float[] vec2){
        if(!sameSize(vec1, vec2)) {
            return null;
        }
        float result; int i = 0;
        while(i < vec1.length){
            result = result + vec1[i] * vec2[i];
            i = i + 1;
        }
        return result;
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







}

