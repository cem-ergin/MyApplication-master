# About the Project

![alt text](https://media.giphy.com/media/fYr1JsxgH6jMLEPYrS/giphy.gif
)

[Flavia](http://flavia.sourceforge.net/) and [Swedish](http://www.cvl.isy.liu.se/en/research/datasets/swedish-leaf/) datasets were used for leaf recognition. The k-NN and SVM algorithms were used as the classification method.

Firstly, the canny algorithm was used to obtain the edge points of the image.
![preprocessing steps](https://i.imgyukle.com/2019/07/02/kvqMAp.png)

Various attributes were obtained from the image where the edge points were obtained;

1- Ratio of leaf width to length

2- Ratio of leaf circumference to length and width

3- The ratio of the area of the leaf to the product of its length and width

4- The ratio of the diameter of the leaf to the sum of the maximum and minimum distances to the center of gravity of the peripheral points

5- The ratio of the distance of the two furthest points of the leaf from each other to the center of gravity

6- The first 50 of 256 LBP attributes.

Image of attributes saved in txt file:

![txt attributes](https://i.imgyukle.com/2019/07/02/kv2ET1.png)

Confusion matrix:

![confusion matrix](https://i.imgyukle.com/2019/07/02/kv2RvI.md.png)

## Installation

After you download and unzip the project, you must enter the Android Studio IDE and run it in the project directory. After running it, you must allow the program installed on your phone to access your files.

## Usage

In the local classification process, which is the first of the three tabs seen at the bottom, you can press the camera button at the bottom right to take a photo or you can classify a leaf image from your gallery. If you want to classify with the server, the server must be turned on.
[Click here for server code](https://github.com/cem-ergin/deneme)

## Contribute
If you'd like to make changes to the project, please open a discussion first.

## License
Project Developers : [Onur Çelebi](https://github.com/onurkou)
, [Cem Ergin](https://github.com/cem-ergin)
, [Ayça Badem](https://github.com/aycabadem)
