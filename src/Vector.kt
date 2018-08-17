class Vector(val x: Int, val y: Int) {
    operator fun plus(other: Vector): Vector {
        return Vector(x + other.x, y + other.y)
    }
    operator fun times(value: Double): Vector {
        return Vector((x * value).toInt(), (y * value).toInt())
    }
    operator fun times(value: Float): Vector {
        return Vector((x * value).toInt(), (y * value).toInt())
    }
    operator fun minus(other: Vector): Vector {
        return Vector(x - other.x, y - other.y)
    }
}