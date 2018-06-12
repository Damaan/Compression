import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;

public class Huffman {

    static String[] CODI = new String[256];


    static void compress(InputStream is, OutputStream os) throws Exception {


        int[] ar = new int[256];
        LinkedList<Byte> lletres = new LinkedList<>();
        LinkedList<Node> sb = new LinkedList<>();
        int caracter;

        //Aquest bucle s'executara mentres quedi informacio que processar.
        while ((caracter = is.read()) != -1) {
            //Per a cada simbol rebut
            //incrementam el valor de la array a la posicio que indica el propi simbol
            byte x = (byte) caracter;
            int x2 = x & 0xff;
            ar[x2]++;
            //Afegim el simbol a la llista de lletres.
            lletres.add((byte) caracter);
        }
        //Per a cada entrada significativa a la array
        //Cream un nou node fulla amb el simbol i el seu comptador
        for (int i = 0; i < ar.length; i++) {
            byte x = (byte) i;
            int x2 = x & 0xff;
            if (ar[i] != 0) { sb.add(new Node(x2,ar[i])); }
        }
        //Mentres quedi mes d'un element a la llsita de Nodes
        //Eliminarem de la llista els dos mes petits per a crear un nou node
        //Aquest nou node sera inserit al final de la llista.
        while (sb.size() > 1){
            Node smaller1 = SearchMinor(sb);sb.remove(smaller1);
            Node smaller2 = SearchMinor(sb);sb.remove(smaller2);
            sb.add(new Node(smaller1,smaller2));
        }
        //Extraiem el node final (arbre) de la llista.
        Node tree = sb.remove();
        String acumulat = "";
        //Amb aquesta funció aconseguim el valor de cada simbol per a comprimir.
        cercaprof(tree,acumulat);
        acumulat = "";
        //Per a cada simbol de l'input original li indicam el seu codi binari.
        while (lletres.size() > 0){
            int z = lletres.remove();
            byte x = (byte) z;
            int x2 = x & 0xff;
                acumulat += CODI[x2];
        }
        //Redondejam el resultat final per a que pugui ser dividit en bytes.
        while ((acumulat.length() % 8) != 0){
            acumulat+= "0";
        }
        LinkedList<String> bytes = new LinkedList<>();
        String result = "";
        //Convertim les dades de la array a una llista de Strings.
        for (int i = 0; i < acumulat.length(); i++) {
            result+=acumulat.charAt(i);
            if (result.length() % 8 == 0){
                bytes.add(result);
                result = "";
            }
        }
        //Per a cada string, el convertim a numero i inserim al resultat final.
        while (bytes.size() > 0){
            os.write(Integer.parseInt(bytes.remove(),2));
        }
        os.flush();
        os.close();
    }

    //Funcio que genera el mapa de l'arbre final.
    private static void cercaprof(Node n, String acumulat) {
        //Si el Node rebut te un fill dret afegirem "1" al resultat
        //i amb aquest fill, tornarem a cridar a aquesta funció
        if(n.right != null){
            acumulat+= "1";
            cercaprof(n.right, acumulat);
            acumulat = acumulat.substring(0,acumulat.length()-1);
        }
        //Si el Node rebut te un fill esquerra afegirem "0" al resultat
        //i amb aquest fill, tornarem a cridar a aquesta funció
        if(n.left != null){
            acumulat+= "0";
            cercaprof(n.left, acumulat);
            acumulat = acumulat.substring(0,acumulat.length()-1);
        }
        //En aquest punt inserim el String acumulat a la array de codis.
            int z = n.getSimbol();
            byte x = (byte) z;
            int x2 = x & 0xff;
            CODI[x2] = acumulat;
    }

    //Funció que cerca el Node menor dins una llista.
    private static Node SearchMinor(LinkedList<Node> sb) {
        Iterator it = sb.iterator();
        Node small = (Node) it.next();
        while (it.hasNext()){
            Node comp = (Node) it.next();
            if (comp.getVegades() < small.getVegades()){
                small = comp;
            }
        }
        return small;
    }

}
