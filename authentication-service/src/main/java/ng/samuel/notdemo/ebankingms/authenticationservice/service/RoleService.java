package ng.samuel.notdemo.ebankingms.authenticationservice.service;


import ng.samuel.notdemo.ebankingms.authenticationservice.dto.PageResponseDTO;
import ng.samuel.notdemo.ebankingms.authenticationservice.dto.RoleRequestDTO;
import ng.samuel.notdemo.ebankingms.authenticationservice.dto.RoleResponseDTO;

public interface RoleService {

    RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO);
    RoleResponseDTO updateRole(Long id, RoleRequestDTO roleRequestDTO);
    void deleteRoleById(Long id);
    RoleResponseDTO getRoleById(Long id);
    RoleResponseDTO getRoleByName(String name);
    PageResponseDTO<RoleResponseDTO> getAllRoles(int page, int size);
    PageResponseDTO<RoleResponseDTO> searchRoles(String query, int page, int size);
}
