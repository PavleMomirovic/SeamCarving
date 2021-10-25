package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.lang.Math.*
import java.util.*
import javax.imageio.ImageIO

fun negative(im: BufferedImage) {
    for (i in 0 until im.width) {
        for (j in 0 until im.height) {
            val tmp = im.getRGB(i, j)
            val col = Color(255 - Color(tmp, true).red, 255 - Color(tmp, true).green, 255 - Color(tmp, true).blue)
            im.setRGB(i, j, col.rgb)
        }
    }
}
fun drawDiagonals(im: BufferedImage) {
    val g2 = im.createGraphics()
    g2.color = Color.RED
    val x = im.width
    val y = im.height
    g2.drawLine(0, 0, x - 1, y - 1)
    g2.drawLine(0, y - 1, x - 1, 0)
}




fun main(args: Array<String>) {
    val fi = File(args[1])
    val fo = File(args[3])
    val w = args[5].toInt()
    val h = args[7].toInt()

    val image: BufferedImage = ImageIO.read(fi)
    val im = ImageForCarving(image)
    im.removeSeamsVertical(w)
    im.removeSeamsHorizontal(h)
    ImageIO.write(im.getImage(), "png", fo)

    //val image: BufferedImage = ImageIO.read(File("test.png"))

}

