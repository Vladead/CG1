import java.awt.*
import javax.swing.*
import kotlin.math.*

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
//        rotateFigure(Math.PI / 3.0, atan(sqrt(2.0)))
//        Timer(17) {
//            rotateFigure(Math.PI / 180.0, 0.0)
//            repaint()
//        }.start()
    }

    private fun scale(s: Double) {
        for (node in nodes) {
            node.x *= s
            node.y *= s
            node.z *= s
        }
    }

    private fun rotateFigure(angleX: Double, angleY: Double) {
        val sinX = sin(angleX)
        val cosX = cos(angleX)
        val sinY = sin(angleY)
        val cosY = cos(angleY)
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

    fun rotateAroundAxis(angle: Double, axisP1: Vector, axisP2: Vector) {
        val axis = axisP2 - axisP1
        val phi = atan2(axis.y, axis.x)
        val theta = atan2(sqrt(axis.x * axis.x + axis.y * axis.y), axis.z)

        val objectMatrix = Matrix(nodes.size, 4)
        for (i in nodes.indices) {
            objectMatrix[i + 1, 1] = nodes[i].x
            objectMatrix[i + 1, 2] = nodes[i].y
            objectMatrix[i + 1, 3] = nodes[i].z
            objectMatrix[i + 1, 4] = 1.0
        }
        val preparationTransform = TransformMatrixFabric.translate(-axisP1.x, -axisP1.y, -axisP1.z) *
                TransformMatrixFabric.rotateZ(-phi) *
                TransformMatrixFabric.rotateY(-theta)
        val returnTransform = TransformMatrixFabric.rotateY(theta) *
                TransformMatrixFabric.rotateZ(phi) *
                TransformMatrixFabric.translate(axisP1.x, axisP1.y, axisP1.z)
        val resultMatrix = objectMatrix * preparationTransform * TransformMatrixFabric.rotateZ(angle) * returnTransform

        for (i in nodes.indices) {
            nodes[i].x = resultMatrix[i + 1, 1]
            nodes[i].y = resultMatrix[i + 1, 2]
            nodes[i].z = resultMatrix[i + 1, 3]
        }
    }

    private fun drawFigure(g: Graphics2D) {
        g.translate(width / 2, height / 2)
        for (edge in edges) {
            val xy1 = nodes[edge[0]]
            val xy2 = nodes[edge[1]]
            g.drawLine(
                xy1.x.roundToInt(), xy1.y.roundToInt(),
                xy2.x.roundToInt(), xy2.y.roundToInt()
            )
        }
        for (node in nodes) {
            g.fillOval(node.x.roundToInt() - 4, node.y.roundToInt() - 4, 8, 8)
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

    fun showRotationAxis(point1: Vector, point2: Vector) {
        graphics.color = Color.ORANGE
        val g = graphics as Graphics2D
        g.translate(width / 2, height / 2)
        g.drawLine(
            point1.x.roundToInt(), point1.y.roundToInt(),
            point2.x.roundToInt(), point2.y.roundToInt()
        )
        g.fillOval(point1.x.roundToInt() - 4, point1.y.roundToInt() - 4, 8, 8)
        g.fillOval(point2.x.roundToInt() - 4, point2.y.roundToInt() - 4, 8, 8)
    }
}

fun main(args: Array<String>) {
    SwingUtilities.invokeLater {
        val frame = JFrame()
        val inputPanel = JPanel()
        inputPanel.layout = BoxLayout(inputPanel, BoxLayout.PAGE_AXIS)

        val firstPointX = JTextField()
        val firstPointY = JTextField()
        val firstPointZ = JTextField()

        val secondPointX = JTextField()
        val secondPointY = JTextField()
        val secondPointZ = JTextField()

        val angle = JTextField()
        val rotateButton = JButton("Повернуть")
        val showAxisButton = JButton("Отобразить ось")

        inputPanel.add(JLabel("X первой точки"))
        inputPanel.add(firstPointX)
        inputPanel.add(JLabel("Y первой точки"))
        inputPanel.add(firstPointY)
        inputPanel.add(JLabel("Z первой точки"))
        inputPanel.add(firstPointZ)
        inputPanel.add(JLabel("X второй точки"))
        inputPanel.add(secondPointX)
        inputPanel.add(JLabel("Y второй точки"))
        inputPanel.add(secondPointY)
        inputPanel.add(JLabel("Z второй точки"))
        inputPanel.add(secondPointZ)
        inputPanel.add(JLabel("Угол"))
        inputPanel.add(angle)
        inputPanel.add(rotateButton)
        inputPanel.add(showAxisButton)

        frame.add(inputPanel, BorderLayout.NORTH)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.title = "Rotating figure"
        frame.isResizable = true
        val rotatingFigure = RotatingFigure()
        frame.add(rotatingFigure, BorderLayout.CENTER)
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true

        showAxisButton.addActionListener {
            rotatingFigure.showRotationAxis(
                Vector(firstPointX.text.toDouble(), firstPointY.text.toDouble(), firstPointZ.text.toDouble()),
                Vector(secondPointX.text.toDouble(), secondPointY.text.toDouble(), secondPointZ.text.toDouble())
            )
        }

        rotateButton.addActionListener {
            rotatingFigure.rotateAroundAxis(
                angle.text.toDouble() / 180 * PI,
                Vector(firstPointX.text.toDouble(), firstPointY.text.toDouble(), firstPointZ.text.toDouble()),
                Vector(secondPointX.text.toDouble(), secondPointY.text.toDouble(), secondPointZ.text.toDouble())
            )
            rotatingFigure.repaint()
            rotatingFigure.showRotationAxis(
                Vector(firstPointX.text.toDouble(), firstPointY.text.toDouble(), firstPointZ.text.toDouble()),
                Vector(secondPointX.text.toDouble(), secondPointY.text.toDouble(), secondPointZ.text.toDouble())
            )
        }
    }
}
