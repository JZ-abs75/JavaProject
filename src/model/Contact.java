package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人类（JavaBean）
 * 用于存储联系人信息
 */
public class Contact {
    private String name;        // 姓名
    private String phoneNumber; // 电话号码

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return name + "  " + phoneNumber;
    }

    /**
     * 获取示例联系人列表（用于默认展示）
     */
    public static List<Contact> getSampleContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("张三", "13800138001"));
        contacts.add(new Contact("李四", "13900139002"));
        contacts.add(new Contact("王五", "13700137003"));
        contacts.add(new Contact("赵六", "13600136004"));
        contacts.add(new Contact("钱七", "13500135005"));
        contacts.add(new Contact("孙八", "13400134006"));
        contacts.add(new Contact("周九", "13300133007"));
        contacts.add(new Contact("吴十", "13200132008"));
        return contacts;
    }
}
