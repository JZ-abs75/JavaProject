package repository;

import model.Contact;
import java.util.List;

/**
 * 联系人数据访问接口
 * 定义 CRUD 操作契约
 */
public interface ContactRepository {

    /** 查询所有联系人 */
    List<Contact> findAll();

    /** 根据ID查询 */
    Contact findById(int id);

    /** 新增联系人（增） */
    boolean insert(Contact contact);

    /** 更新联系人（改） */
    boolean update(Contact contact);

    /** 删除联系人（删） */
    boolean delete(int id);
}
