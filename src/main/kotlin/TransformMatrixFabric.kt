import kotlin.math.cos
import kotlin.math.sin

object TransformMatrixFabric {
    fun rotateX(angle: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[3, 3] = 1.0
        matrix[0, 0] = 1.0
        matrix[1, 1] = cos(angle)
        matrix[1, 2] = sin(angle)
        matrix[2, 1] = -sin(angle)
        matrix[2, 2] = cos(angle)
        return matrix
    }

    fun rotateY(angle: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[3, 3] = 1.0
        matrix[1, 1] = 1.0
        matrix[0, 0] = cos(angle)
        matrix[0, 2] = -sin(angle)
        matrix[2, 0] = sin(angle)
        matrix[2, 2] = cos(angle)
        return matrix
    }

    fun rotateZ(angle: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[3, 3] = 1.0
        matrix[2, 2] = 1.0
        matrix[0, 0] = cos(angle)
        matrix[0, 1] = sin(angle)
        matrix[1, 0] = -sin(angle)
        matrix[1, 1] = cos(angle)
        return matrix
    }

    fun translate(x: Double, y: Double, z: Double): Matrix {
        val matrix = Matrix(4, 4)
        matrix[0,0] = 1.0
        matrix[1,1] = 1.0
        matrix[2,2] = 1.0
        matrix[3,3] = 1.0
        matrix[3,0] = x
        matrix[3,1] = y
        matrix[3,2] = z
        return matrix
    }
}