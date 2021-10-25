# SeamCarving
Program reduces the picture by removing least important pixels from it. It first calculates energy (importance) of each pixel, then based on that finds seams (horisontal and vertical) that are of lowest importance and deletes them. It will delete as many lowest seams as specifiend in the input.

input should be from cmd as following:
java Main -in trees.png -out trees-reduced.png -width 100 -height 30


Inside the ImageForCarving class you can also find grayScaleEnergyRepresentation function that was used for testing during the programming process. 
