package seamcarving


import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt

class ImageForCarving(private val im : BufferedImage) {

    private val intensityMat = Array<Array<Int>>(im.width) { Array<Int>(im.height) { 0 } }

    private fun pixelEnergy(x: Int, y: Int): Double {
        var xm = x
        var ym = y
        if (xm == 0) xm++
        if (xm == im.width - 1) xm--
        val dx = ((Color(im.getRGB(xm + 1, y)).red - Color(im.getRGB(xm - 1, y)).red).toDouble()).pow(2.0) +
                ((Color(im.getRGB(xm + 1, y)).green - Color(im.getRGB(xm - 1, y)).green).toDouble()).pow(2.0) +
                ((Color(im.getRGB(xm + 1, y)).blue - Color(im.getRGB(xm - 1, y)).blue).toDouble()).pow(2.0)
        if (y == 0) ym++
        if (y == im.height - 1) ym--
        val dy = ((Color(im.getRGB(x, ym + 1)).red - Color(im.getRGB(x, ym - 1)).red).toDouble()).pow(2.0) +
                ((Color(im.getRGB(x, ym + 1)).green - Color(im.getRGB(x, ym - 1)).green).toDouble()).pow(2.0) +
                ((Color(im.getRGB(x, ym + 1)).blue - Color(im.getRGB(x, ym - 1)).blue).toDouble()).pow(2.0)

        return sqrt(dx + dy)
    }

    fun grayScaleEnergyRepresentation(): BufferedImage {
        val out: BufferedImage = im
        makeIntensityMatrix()
        for (i in 0 until im.width) {
            for (j in 0 until im.height) {
                val k = intensityMat[i][j]
                out.setRGB(i, j, Color(k,k,k).rgb)
            }
        }
        return out
    }

    fun makeIntensityMatrix(){
        var maxEn = 0.0
        val helpMat = Array<Array<Double>>(im.width) { Array<Double>(im.height) { 0.0 } }


        //calculating energy and preparing:
        for (i in 0 until im.width) {
            for (j in 0 until im.height) {
                helpMat[i][j] = pixelEnergy(i, j)
                if (helpMat[i][j] > maxEn) maxEn = helpMat[i][j]
            }
        }

        // normalization and making:
        for (i in 0 until im.width) {
            for (j in 0 until im.height) {
                intensityMat[i][j] = (255.0 * helpMat[i][j] / maxEn).toInt()
            }
        }

    }
    //val m = im.width
    //val n = im.height
    //val pq = PriorityQueue<Pixel>(m*n)



}