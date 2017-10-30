package com.example.model;

import com.example.model.exceptions.*;

/**
 * Created by Marina on 15.10.2017 ?..
 */
public class Category {
	private final static int MAX_LENGTH = 255;
	private long id;
	private String name;

	// * constructor to be used when putting object in database
	Category(String name) throws CategoryException {
		this.setName(name);
	}

	// * constructor to be used for fetching from database
	public Category(long id, String name) throws CategoryException {
		this(name);
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public long getId() {
		return this.id;
	}

	public void setName(String name) throws CategoryException {
		if (!name.isEmpty()) {
			if (name.length() > MAX_LENGTH) {
				throw new CategoryException(
						"Name of the category is too long. It should be less than" + MAX_LENGTH + " symbols");
			}
		} else {
			throw new CategoryException("Name of the category should not be empty!");
		}
		this.name = name;
	}

	public void setId(long id) throws CategoryException {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Category category = (Category) o;

		if (id != category.id)
			return false;
		return name.equals(category.name);
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + name.hashCode();
		return result;
	}
}
