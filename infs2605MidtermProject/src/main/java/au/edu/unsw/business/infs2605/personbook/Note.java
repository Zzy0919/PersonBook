package au.edu.unsw.business.infs2605.personbook;

public class Note {

    private int id;

    private int personId;

    private String createTime;

    private String content;

    Note(int id, int personId, String createTime, String content){
        this.id = id;
        this.personId = personId;
        this.createTime = createTime;
        this.content = content;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setPersonId(int personId){
        this.personId = personId;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setContent(String content){
        this.content = content;
    }

    public int getId(){
        return this.id;
    }

    public int getPersonId(){
        return this.personId;
    }

    public String getCreateTime(){
        return this.createTime;
    }

    public String getContent(){
        return this.content;
    }
}
