public class Token {

    private byte simbol;
    private int index;

    public Token(byte simbol, int index){
        this.simbol = simbol;
        this.index = index;
    }

    @Override
    public String toString() {
        return simbol +" "+ index;
    }

    @Override
    public boolean equals(Object obj) {
        Token k = (Token) obj;
        return (this.simbol == k.simbol && this.index == k.index);
    }

    public byte getSimbol() { return simbol; }
    public int getIndex() { return index; }
}
