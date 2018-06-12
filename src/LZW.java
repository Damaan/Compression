import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

public class LZW {


    public static void compress(InputStream is, OutputStream os) throws Exception {

        Token[] ar = new Token[257];
        int caracter;
        int count = 1;
        int c;
        Token x;
        LinkedList<Byte> sb = new LinkedList<>();

        //Guardam els bytes de l'imputstream a una llista.
        while ((caracter = is.read()) != -1) {
            sb.add((byte) caracter);
        }

        //Aquest bucle s'executara mentres la llista contengui valors.
        while (sb.size() > 0) {

            //Treiem de la llista un byte i l'empram per crear un objecte Token.
            caracter = sb.remove();
            x = new Token((byte) caracter, 0);

            //Mentres trobem el token x a la array
            while ((c = search(x, ar)) != 0) {
                //Treiem de la llista un altre byte, en cas de que hi quedin.
                //Despres crea, un nou token amb el resultat de c(index del Token trobat)
                //i cream un nou Token
                if (sb.size() > 0) {
                    caracter = sb.remove();
                    x = new Token((byte) caracter, c);
                    //En cas que no quedin,rompem es bucle.
                } else { break; }
            }
            //Inserim el Token nou a la array.
            ar[count] = x;
            //En cas de que la array sigui plena, la buidam
            if (ar[256] != null) {
                os = neteja(os,ar);
                ar = new Token[257];
                count = 1;
            } else {
                count++;
            }
        }
        //Despres del bucle si queda informació a la array la inserim al resultat.
        os = neteja(os,ar);
        os.flush();
        os.close();
    }

    public static void decompress(InputStream is, OutputStream os) throws Exception {

        Token[] ar = new Token[257];
        int caracter;
        int count = 1;
        Token x;
        LinkedList<Byte> sb = new LinkedList<>();

        //Aquest bucle s'executara mentres quedi informacio a is
        while ((caracter = is.read()) != -1) {
            //Simplement cream un nou Token amb dos valors de l'InputStream
            //Despres l'inserim a la array i incrementam comptador
            x = new Token((byte) is.read(), caracter);
            ar[count] = x;
            count++;
            //En el moment que la array quedi plena, guardam les dades
            //Despres reinicialitzam la array i el comptador
            if (count == 257) {
                sb = guarda(ar, sb);
                ar = new Token[257];
                count = 1;
            }
        }
        //Guardam les dades finals al OutputStream.
        sb = guarda(ar, sb);
        while (sb.size() > 0) { os.write(sb.remove()); }
        os.flush();
        os.close();
    }

    //Funcio per a guardar la informacio de una array de Token a una llista.
    private static LinkedList<Byte> guarda(Token[] ar, LinkedList<Byte> sb) {

        LinkedList<Byte> sb2 = new LinkedList<>();
        int c;
        for (Token anAr : ar) {
            if (anAr != null) {
                sb2.push(anAr.getSimbol());
                c = anAr.getIndex();
                while (c != 0) {
                    sb2.push(ar[c].getSimbol());
                    c = ar[c].getIndex();
                }
                while (sb2.size() > 0) { sb.add(sb2.pop()); }
            }
        }
        return sb;
    }

    //Funció que cerca un Token en una array
    //si el troba retorna el seu index.
    private static int search(Token x, Token[] ar) {
        for (int i = 0; i < ar.length; i++) {
            Token k = ar[i];
            if (k != null) {
                if (x.equals(k)) {
                    return i;
                }
            }
        }
        return 0;
    }

    //Funcio que simplement guarda els valors de la array dins l'OutputStream.
    private static OutputStream neteja(OutputStream os, Token[] ar) throws IOException {
        for (Token k : ar) {
            if (k != null) {
                os.write(k.getIndex());
                os.write(k.getSimbol());
            }
        }
        return os;
    }


}
