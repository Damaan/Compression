import java.io.InputStream;
import java.io.OutputStream;

public class RLE {

    public static void compress(InputStream is, OutputStream os) throws Exception {
        int caracter;
        int veces = 0;
        int old = 0;

        //Aquest bucle s'efectua mentres a l'imputstream quedi informació
        while ((caracter = is.read()) != -1) {
            //Si el nou caracter es igual a l'anterior,incrementam el comptador.
            if (old == caracter) {
                veces++;
                //En cas de que hagi aparescut 1 o 2 vegades,l'escrivim a l'outputstream
                if (veces <= 2) {
                    os.write(caracter);
                    //Si ha aparecut 257 vegades,escrivim un 255 i resetejam a 0 el comptador.
                } else if (veces == 257) {
                    os.write(veces - 2);
                    veces = 0;
                }
                //En cas de que el nou caracter es diferent de l'anterior
            } else {
                //Si el comptador es mayor 0 igual a 2 escrivim el comptador-2
                if (veces >= 2) {
                    os.write(veces - 2);
                }
                //Despres escrivim el caracter nou, el definim com antic i resetejam el comptador
                os.write(caracter);
                old = caracter;
                veces = 1;
            }
        }
        //En cas de que el bucle acabi i el comptador sigui mayor a 1, escrivim al resultat comptador-2
        if (veces > 1) {
            os.write(veces - 2);
        }
        os.flush();
        os.close();
    }

    public static void decompress(InputStream is, OutputStream os) throws Exception {
        int caracter;
        int veces = 0;
        int old = 0;
        //Aquest bucle s'efectua mentres a l'imputstream quedi informació
        //Quan llegim un caracter,incrementam el comptador.
        while ((caracter = is.read()) != -1) {
            veces++;
            //Si el nou caracter es igual a l'anterior:
            if (old == caracter) {
                //Si el comptador es menor o igual a 2,escrivim al resultat el caracter.
                if (veces <= 2) {
                    os.write(caracter);
                    //Si apareix 3 vegades seguides, escrivim el byte tantes vegades com valor tengui.
                    //Despres resetejam el comptador a 0
                } else if (veces == 3) {
                    for (int i = 0; i < caracter; i++) {
                        os.write(caracter);
                    }
                    veces = 0;
                }
                //En cas de que el caracter sigui diferent a l'antic
            } else {
                //Si es el tercer caracter a apareixer, escribim el byte tantes vegades com valor tengui.
                if (veces == 3) {
                    for (int i = 0; i < caracter; i++) {
                        os.write(old);
                    }
                    //Despres resetejam comptador a 0 i definim el valor del caracter anterior com l'actual.
                    veces = 0;
                    old = caracter;
                    //En cas de que hagi aparescut menys de 3 vegades
                    //escribim el caracter,actualizam el valor de l'antic i resetejam el comptador a 1
                } else {
                    os.write(caracter);
                    old = caracter;
                    veces = 1;
                }
            }
        }
        os.flush();
        os.close();
    }
}
