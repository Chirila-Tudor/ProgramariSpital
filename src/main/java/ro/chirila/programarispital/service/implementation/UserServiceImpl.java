package ro.chirila.programarispital.service.implementation;

import org.jvnet.hk2.annotations.Service;
import org.modelmapper.ModelMapper;
import ro.chirila.programarispital.exception.UserAlreadyExistException;
import ro.chirila.programarispital.repository.UserRepository;
import ro.chirila.programarispital.repository.dto.UserResponseDTO;
import ro.chirila.programarispital.repository.entity.User;

@Service
public class UserServiceImpl{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserResponseDTO addUser(String username){
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistException("User already exist!");
        }
        User user = new User();
        user.setUsername(username);
        user.setHasPassword(false);
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
