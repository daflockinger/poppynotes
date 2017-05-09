package com.flockinger.poppynotes.userService.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.poppynotes.userService.dao.UserRepository;
import com.flockinger.poppynotes.userService.dto.CreateUser;
import com.flockinger.poppynotes.userService.dto.ShowUser;
import com.flockinger.poppynotes.userService.dto.UpdateUser;
import com.flockinger.poppynotes.userService.exception.DuplicateUserException;
import com.flockinger.poppynotes.userService.exception.UserNotFoundException;
import com.flockinger.poppynotes.userService.model.User;
import com.flockinger.poppynotes.userService.service.EditUserService;

@Service
public class EditUserServiceImpl implements EditUserService{

	@Autowired
	private UserRepository dao;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	@Transactional
	public void createUser(CreateUser user) throws DuplicateUserException {
		if(isUserwithAuthEmailAlreadyExisting(user.getAuthEmail())) {
			throw new DuplicateUserException("User with authorization email: " + user.getAuthEmail() + " exists already" );
		}
		
		dao.save(map(user));
	}
	
	private boolean isUserwithAuthEmailAlreadyExisting(String authEmail) {
		return dao.findByAuthEmail(authEmail) != null;
	}

	@Override
	@Transactional
	public void updateUser(UpdateUser user) throws UserNotFoundException, DuplicateUserException {
		if(!dao.exists(user.getId())) {
			throw new UserNotFoundException("Cannot find user with ID: " + user.getId());
		}
		if(isThereAnotherUserWithAuthEmail(user)) {
			throw new DuplicateUserException("Cannot change Authorization-Email to that of another user's!");
		}
		
		dao.save(map(user));
	}
	
	private boolean isThereAnotherUserWithAuthEmail(UpdateUser user) {
		User similarUser =  dao.findByAuthEmail(user.getAuthEmail());
		return similarUser != null && similarUser.getId() != user.getId();
	}

	@Override
	@Transactional
	public void deleteUser(Long userId) throws UserNotFoundException {
		if(!dao.exists(userId)) {
			throw new UserNotFoundException("Cannot find user with ID: " + userId);
		}
		
		dao.delete(userId);
	}

	@Override
	@Transactional(readOnly=true)
	public ShowUser getUserById(Long userId) throws UserNotFoundException {
		if(!dao.exists(userId)) {
			throw new UserNotFoundException("Cannot find user with ID: " + userId);
		}
		return map(dao.findOne(userId));
	}
	
	
	private ShowUser map(User user) {
		return mapper.map(user, ShowUser.class);
	}
	
	private User map(CreateUser createUser) {
		return mapper.map(createUser, User.class);
	}
	
	private User map(UpdateUser updateUser) {
		return mapper.map(updateUser, User.class);
	}
}
