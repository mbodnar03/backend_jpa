package com.jpacourse.persistance.dao.impl;

import com.jpacourse.persistance.dao.Dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractDao<T, K extends Serializable> implements Dao<T, K> {

	@PersistenceContext
	protected EntityManager entityManager;

	private final Class<T> entityClass;

	protected AbstractDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T save(T entity) {
		entityManager.persist(entity);
		return entity;
	}

	@Override
	public T getOne(K id) {
		return entityManager.getReference(entityClass, id);
	}

	@Override
	public T findOne(K id) {
		return entityManager.find(entityClass, id);
	}

	@Override
	public List<T> findAll() {
		return entityManager.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
	}

	@Override
	public T update(T entity) {
		return entityManager.merge(entity);
	}

	@Override
	public void delete(T entity) {
		entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
	}

	@Override
	public void delete(K id) {
		T entity = findOne(id);
		if (entity != null) {
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		entityManager.createQuery("DELETE FROM " + entityClass.getSimpleName()).executeUpdate();
	}

	@Override
	public long count() {
		return entityManager.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class).getSingleResult();
	}

	@Override
	public boolean exists(K id) {
		return findOne(id) != null;
	}
}
