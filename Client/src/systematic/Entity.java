package systematic;

public abstract class Entity {
    int opLevel=0;
    boolean isRemoved=false;

    //An approach of indirect deleting

    //To mark a entity as removed (Student, Teacher, etc..)
    public void remove() {
        isRemoved = true;
    }
    //Containers, when iterating through all its entries, would remove manually all isRemoved() entries.
    public boolean isRemoved(){
        return isRemoved;
    }
}
