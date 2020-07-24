public class vertex {
    boolean visited=false;
    int index;
    int x;
    int y;
vertex(int index,int x,int y){
    this.index=index;
    this.x=x;
    this.y=y;
}
    public int getIndex() {
        return this.index;
    }



    public int getX() {
        return this.x;
    }



    public int getY() {
        return this.y;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
