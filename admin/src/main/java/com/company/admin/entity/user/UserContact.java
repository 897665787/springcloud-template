package com.company.admin.entity.user;

import java.util.List;

import com.company.admin.jackson.annotation.AutoDesc;

/**
 * 联系人
 * Created by wjc on 2018/11/19.
 */
public class UserContact extends UserContactBook {

	/**
	 * 通讯录中是否存在，0-否，1-是
	 */
	@AutoDesc({"0:否", "1:是"})
	private Integer inBook;

	private List<UserContact> contactList;

	public static final Integer IN_BOOK=1, NOT_IN_BOOK = 0;

    public UserContact() {
    }

    public UserContact(String userId) {
        super(userId);
    }

    public Integer getInBook() {
		return inBook;
	}

	public void setInBook(Integer inBook) {
		this.inBook = inBook;
	}

    public List<UserContact> getContactList() {
        return contactList;
    }

    public void setContactList(List<UserContact> contactList) {
        this.contactList = contactList;
    }
}
