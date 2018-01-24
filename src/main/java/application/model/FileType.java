package application.model;

/**
 * Created by guimu-work on 2018/1/10.
 */
public enum FileType {

    RADAR("�״�"), IMAGE("���");

    private String name;

    // ���췽��
    private FileType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
