import kotlin.math.cos
import kotlin.math.sin

object TransformMatrixFabric {
    fun rotateX(angle: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[4, 4] = 1.0
        matrix[1, 1] = 1.0
        matrix[2, 2] = cos(angle)
        matrix[2, 3] = sin(angle)
        matrix[3, 2] = -sin(angle)
        matrix[3, 3] = cos(angle)
        return matrix
    }

    fun rotateY(angle: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[4, 4] = 1.0
        matrix[2, 2] = 1.0
        matrix[1, 1] = cos(angle)
        matrix[1, 3] = -sin(angle)
        matrix[3, 1] = sin(angle)
        matrix[3, 3] = cos(angle)
        return matrix
    }

    fun rotateZ(angle: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[4, 4] = 1.0
        matrix[3, 3] = 1.0
        matrix[1, 1] = cos(angle)
        matrix[1, 2] = sin(angle)
        matrix[2, 1] = -sin(angle)
        matrix[2, 2] = cos(angle)
        return matrix
    }

    fun translate(x: Double, y: Double, z: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[1,1] = 1.0
        matrix[2,2] = 1.0
        matrix[3,3] = 1.0
        matrix[4,4] = 1.0
        matrix[4,1] = x
        matrix[4,2] = y
        matrix[4,3] = z
        return matrix
    }

    fun perspectiveZ(z: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[1,1] = 1.0
        matrix[2,2] = 1.0
        matrix[3,3] = 1.0
        matrix[4,4] = 1.0
        matrix[3,4] = z
        return matrix
    }

    fun perspectiveY(y: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[1,1] = 1.0
        matrix[2,2] = 1.0
        matrix[3,3] = 1.0
        matrix[4,4] = 1.0
        matrix[2,4] = y
        return matrix
    }

    fun perspectiveX(x: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[1,1] = 1.0
        matrix[2,2] = 1.0
        matrix[3,3] = 1.0
        matrix[4,4] = 1.0
        matrix[1,4] = x
        return matrix
    }
}