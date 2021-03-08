data class Vector(var x: Double, var y: Double, var z: Double) {

    operator fun plus(b: Vector) = Vector(x + b.x, y + b.y, z + b.z)

    operator fun minus(b: Vector) = Vector(x - b.x, y - b.y, z - b.z)
}