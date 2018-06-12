public class Node {

    private int Simbol = -1;
    private int vegades;
    Node right;
    Node left;

    //Constructor per a actuar com a rama.
    Node(Node left, Node right){
        this.right = right;
        this.left = left;
        this.vegades = left.vegades + right.vegades;
    }

    //Constructor per a actuar com a fulla.
    Node(int Simbol, int vegades){
        this.Simbol = Simbol;
        this.vegades = vegades;
    }

    @Override
    public String toString() {
        return this.Simbol + " " + this.vegades + " " + this.right + " " + this.left;
    }

    public int getSimbol() { return Simbol; }
    public int getVegades() { return vegades; }


}
