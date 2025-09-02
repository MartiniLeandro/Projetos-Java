package My_Tasks.demo.entities.DTOS;

import My_Tasks.demo.entities.User;

public record UserDTO(Long id, String name, String email) {
    public UserDTO(User user){
       this(user.getId(), user.getName(), user.getEmail());
    }
}
