package seamcarving


import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class ImageForCarving(private val im : BufferedImage) {

    private val maxEn =0
    private val intensityMat = Array<Array<Double>>(im.width) { Array<Double>(im.height) { 0.0 } }

    private fun pixelEnergy(x: Int, y: Int): Double {
        var xm = x
        var ym = y
        if (xm == 0) xm++
        if (xm == im.width - 1) xm--
        val dx = Math.pow(((Color(im.getRGB(xm + 1, y)).red - Color(im.getRGB(xm - 1, y)).red).toDouble()),2.0) +
                Math.pow(((Color(im.getRGB(xm + 1, y)).green - Color(im.getRGB(xm - 1, y)).green).toDouble()),2.0)+
                Math.pow(((Color(im.getRGB(xm + 1, y)).blue - Color(im.getRGB(xm - 1, y)).blue).toDouble()),2.0)
        if (y == 0) ym++
        if (y == im.height - 1) ym--
        val dy = Math.pow(((Color(im.getRGB(x, ym + 1)).red - Color(im.getRGB(x, ym - 1)).red).toDouble()),2.0) +
                Math.pow(((Color(im.getRGB(x, ym + 1)).green - Color(im.getRGB(x, ym - 1)).green).toDouble()),2.0) +
                Math.pow(((Color(im.getRGB(x, ym + 1)).blue - Color(im.getRGB(x, ym - 1)).blue).toDouble()),2.0)

        return Math.sqrt(dx + dy)
    }

    fun grayScaleEnergyRepresentation(): BufferedImage {
        val out: BufferedImage = im
        makeIntensityMatrix()
        for (i in 0 until im.width) {
            for (j in 0 until im.height) {
                val k = (255.0 * intensityMat[i][j] / maxEn).toInt()
                out.setRGB(i, j, Color(k,k,k).rgb)
            }
        }
        return out
    }

    fun makeIntensityMatrix(){
        var maxEn = 0.0

        //calculating energy and preparing:
        for (i in 0 until im.width) {
            for (j in 0 until im.height) {
                intensityMat[i][j] = pixelEnergy(i, j)
                if (intensityMat[i][j] > maxEn) maxEn = intensityMat[i][j]
            }
        }
    }

    val m = im.width
    val n = im.height
    private var pq = PriorityQueue<Pixel>(m*n)
    private var visited = mutableMapOf<String,Pixel>()
    private var processed = mutableMapOf<String,Pixel>()

    private fun checkPixelForMarkSeam(x:Int, y:Int, parent : Pixel){
        if(!processed.containsKey("${x}x${y}")){
            if(visited.containsKey("${x}x${y}")){
                val change = visited["${x}x${y}"]!!
                if(parent.intensitySum+change.intensity<change.intensitySum){
                    pq.remove(change)
                    change.intensitySum=parent.intensitySum+change.intensity
                    change.parent=parent
                    pq.add(change)
                }
            } else{
                val newPixel = Pixel(x,y,intensityMat[x][y],parent)
                newPixel.intensitySum=parent.intensitySum+newPixel.intensity
                visited["${newPixel.x}x${newPixel.y}"] = newPixel
                pq.add(newPixel)
            }
        }
    }

    fun markSeamVertical(): BufferedImage {  //Dijsktra algoritam sa modifikacijama
        val image = im
        for(i in 0 until m) {
            val newPixel = Pixel(i,0,intensityMat[i][0],null)
            newPixel.intensitySum=newPixel.intensity
            pq.add(newPixel)
            visited["${newPixel.x}x${newPixel.y}"] = newPixel
        }

        var tmp = Pixel(0,0,0.00001,null)

        while(true){
            if(pq.isEmpty()) break
            tmp = pq.poll()

            processed["${tmp.x}x${tmp.y}"] = tmp
            visited.remove("${tmp.x}x${tmp.y}")
          //  image.setRGB(tmp.x,tmp.y,Color.BLACK.rgb) //za test

            if(tmp.y==n-1) break

            if(tmp.x>0) checkPixelForMarkSeam(tmp.x-1,tmp.y+1,tmp)
            checkPixelForMarkSeam(tmp.x,tmp.y+1,tmp)
            if(tmp.x<m-1) checkPixelForMarkSeam(tmp.x+1,tmp.y+1,tmp)

        }

        while(true){
            image.setRGB(tmp.x,tmp.y,Color.RED.rgb)
            tmp=tmp.parent?:break
        }
        return image
    }

    fun markSeamHorizontal(): BufferedImage{  //Dijsktra algoritam sa modifikacijama
        val image = im
        for(j in 0 until n) {
            val newPixel = Pixel(0,j,intensityMat[0][j],null)
            newPixel.intensitySum=newPixel.intensity
            pq.add(newPixel)
            visited["${newPixel.x}x${newPixel.y}"] = newPixel
        }

        var tmp = Pixel(0,0,0.00001,null)

        while(true){
            if(pq.isEmpty()) break
            tmp = pq.poll()

            processed["${tmp.x}x${tmp.y}"] = tmp
            visited.remove("${tmp.x}x${tmp.y}")
            //  image.setRGB(tmp.x,tmp.y,Color.BLACK.rgb) //za test

            if(tmp.x==m-1) break

            if(tmp.y>0) checkPixelForMarkSeam(tmp.x+1,tmp.y-1,tmp)
            checkPixelForMarkSeam(tmp.x+1,tmp.y,tmp)
            if(tmp.y<n-1) checkPixelForMarkSeam(tmp.x+1,tmp.y+1,tmp)

        }

        while(true){
            image.setRGB(tmp.x,tmp.y,Color.RED.rgb)
            tmp=tmp.parent?:break
        }
        return image
    }



}