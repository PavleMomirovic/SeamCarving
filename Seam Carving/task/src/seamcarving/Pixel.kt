package seamcarving



class Pixel(val x: Int,val y:Int,val intensity:Double, var parent: Pixel?) : Comparable<Pixel> {
    var intensitySum = Double.MAX_VALUE
    override fun compareTo(other: Pixel): Int {
        return  (intensitySum.toInt() -other.intensitySum.toInt())
    }
}

