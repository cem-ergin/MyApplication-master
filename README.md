Click [here](https://github.com/cem-ergin/MyApplication-master/blob/master/READMEnglish.md) for english readme file.

# Proje Hakkında

![alt text](https://media.giphy.com/media/fYr1JsxgH6jMLEPYrS/giphy.gif
)

[Flavia](http://flavia.sourceforge.net/) ve [Swedish](http://www.cvl.isy.liu.se/en/research/datasets/swedish-leaf/) verisetleri kullanılarak yaprak tanıma uygulaması yapılması amaçlanmıştır. Sınıflandırma yöntemi olarak k-NN ve SVM algoritmaları kullanılmıştır.

Öncelikle canny algoritması kullanılarak resmin kenar noktaları elde edildi.
![önişleme adımları](https://i.imgyukle.com/2019/07/02/kvqMAp.png)

Kenar noktaları elde edilen resimden çeşitli öznitelikler çıkartıldı;

1- Yaprağın eninin boyuna oranı

2- Yaprağın çevresinin boyu ile eninin toplamına oranı

3- Yaprağın alanının boyu ile eninin çarpımına oranı

4- Yaprağın çapının, çevre noktalarının ağırlık merkezine olan maksimum ve mininmum uzaklıklarının toplamına oranı

5- Yaprağın birbirine olan en uzak iki noktasının, ağırlık merkezine olan uzaklıkları oranı

6- 256 adet LBP özniteliğinin ilk 50 tanesi.

Txt dosyasına kayıt edilen özniteliklerin görüntüsü:

![txt öznitelikler](https://i.imgyukle.com/2019/07/02/kv2ET1.png)

Confusion matrisi:

![confusion matrix](https://i.imgyukle.com/2019/07/02/kv2RvI.md.png)

## Yükleme

Projeyi indirip zipten çıkardıktan sonra Android Studio IDE'sine girip projenin dizinine girip çalıştırmalısınız. Çalıştırdıktan sonra telefonunuza yüklenen programın dosyalarınıza erişmesine izin vermelisiniz. 

## Kullanım

En altta görülen üç sekmeden ilki olan localde sınıflandırma işleminde sağ alttaki kamera tuşuna basarak ister fotoğraf çekin, ister galerinizden bir yaprak görüntüsünü sınıflandırabilirsiniz. Server ile sınıflandırma yapmak isterseniz server'ın açık olması gerekir.
[Server Kodları için tıklayın](https://github.com/cem-ergin/deneme)

## Katkıda Bulunmak İçin
Projede değişiklik yapmak isterseniz, lütfen ilk önce tartışma bölümü açın.

## Lisans
Projeyi yapanlar : [Onur Çelebi](https://github.com/onurkou)
, [Cem Ergin](https://github.com/cem-ergin)
, [Ayça Badem](https://github.com/aycabadem)
