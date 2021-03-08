import java.awt.*
import javax.swing.*
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.sqrt

class RotatingFigure : JPanel() {
    private val nodes = arrayOf(
        Vector(-1.0, -1.0, -1.0),
        Vector(-1.0, 1.0, 1.0),
        Vector(1.0, -1.0, 1.0),
        Vector(1.0, 1.0, -1.0)
    )
    private val edges = arrayOf(
        intArrayOf(0, 1),
        intArrayOf(1, 2),
        intArrayOf(2, 0),
        intArrayOf(0, 3),
        intArrayOf(3, 2),
        intArrayOf(3, 1)
    )

    init {
        preferredSize = Dimension(640, 640)
        background = Color.white
        scale(100.0)
        rotateFigure(Math.PI / 3.0, Math.atan(Math.sqrt(2.0)))
        Timer(17) {
            rotateFigure(Math.PI / 180.0, 0.0)
            repaint()
        }.start()
    }

    private fun scale(s: Double) {
        for (node in nodes) {
            node.x *= s
            node.y *= s
            node.z *= s
        }
    }

    private fun rotateFigure(angleX: Double, angleY: Double) {
        val sinX = Math.sin(angleX)
        val cosX = Math.cos(angleX)
        val sinY = Math.sin(angleY)
        val cosY = Math.cos(angleY)
        for (node in nodes) {
            val x = node.x
            val y = node.y
            var z = node.z
            node.x = x * cosX - z * sinX
            node.z = z * cosX + x * sinX
            z = node.z
            node.y = y * cosY - z * sinY
            node.z = z * cosY + y * sinY
        }
    }

    private fun rotateAroundAxis(angle: Double, axisP1: Vector, axisP2: Vector) {
        val axis = axisP2 - axisP1
        val theta = atan(axis.y/axis.x)
        val phi = atan(sqrt(axis.x*axis.x+axis.y*axis.y)/axis.z)

        val objectMatrix = Matrix(nodes.size, 4)
        for (i in nodes.indices) {
            objectMatrix[i, 0] = nodes[i].x
            objectMatrix[i, 1] = nodes[i].y
            objectMatrix[i, 2] = nodes[i].z
            objectMatrix[i, 3] = 1.0
        }
        val preparationTransform = TransformMatrixFabric.translate(-axisP1.x, -axisP1.y, -axis.z) *
                TransformMatrixFabric.rotateZ(-phi) *
                TransformMatrixFabric.rotateY(PI/2.0 - theta)
        val returnTransform = TransformMatrixFabric.rotateY(theta - PI/2)*
                TransformMatrixFabric.rotateZ(phi)*
                TransformMatrixFabric.translate(axisP1.x, axisP1.y, axisP2.z)
        val resultMatrix = objectMatrix * preparationTransform * TransformMatrixFabric.rotateZ(angle) * returnTransform

        for (i in nodes.indices) {
            nodes[i].x = resultMatrix[i, 0]
            nodes[i].y = resultMatrix[i, 1]
            nodes[i].z = resultMatrix[i, 2]
        }
    }

    private fun drawFigure(g: Graphics2D) {
        g.translate(width / 2, height / 2)
        for (edge in edges) {
            val xy1 = nodes[edge[0]]
            val xy2 = nodes[edge[1]]
            g.drawLine(
                Math.round(xy1.x).toInt(), Math.round(xy1.y).toInt(),
                Math.round(xy2.x).toInt(), Math.round(xy2.y).toInt()
            )
        }
        for (node in nodes) {
            g.fillOval(Math.round(node.x).toInt() - 4, Math.round(node.y).toInt() - 4, 8, 8)
            g.color = Color.ORANGE
        }
    }

    public override fun paintComponent(gg: Graphics) {
        super.paintComponent(gg)
        val g = gg as Graphics2D
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.color = Color.blue
        drawFigure(g)
    }
}

fun main(args: Array<String>) {
    SwingUtilities.invokeLater {
        val f = JFrame()
        f.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        f.title = "Rotating figure"
        f.isResizable = false
        f.add(RotatingFigure(), BorderLayout.CENTER)
        f.pack()
        f.setLocationRelativeTo(null)
        f.isVisible = true
    }
}
