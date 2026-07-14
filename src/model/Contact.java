package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人类（JavaBean）
 * 用于存储联系人信息
 */
public class Contact {
    private int id;             // 数据库ID（-1表示新联系人）
    private String name;        // 姓名
    private String phoneNumber; // 电话号码

    public Contact(String name, String phoneNumber) {
        this(-1, name, phoneNumber);
    }

    public Contact(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return name + "  " + phoneNumber;
    }

    /**
     * 获取示例联系人列表（数据库不可用时回退）
     */
    public static List<Contact> getSampleContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "张三", "13800138001"));
        contacts.add(new Contact(2, "李四", "13900139002"));
        contacts.add(new Contact(3, "王五", "13700137003"));
        contacts.add(new Contact(4, "赵六", "13600136004"));
        contacts.add(new Contact(5, "钱七", "13500135005"));
        contacts.add(new Contact(6, "孙八", "13400134006"));
        contacts.add(new Contact(7, "周九", "13300133007"));
        contacts.add(new Contact(8, "吴十", "13200132008"));
        return contacts;
    }
}
