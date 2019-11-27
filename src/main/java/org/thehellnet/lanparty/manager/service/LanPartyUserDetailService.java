package org.thehellnet.lanparty.manager.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Privilege;
import org.thehellnet.lanparty.manager.model.persistence.Role;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
public class LanPartyUserDetailService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public LanPartyUserDetailService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(username);

        if (appUser == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(
                appUser.getEmail(),
                appUser.getPassword(),
                appUser.getEnabled(),
                true,
                true,
                true,
                getAuthorities(appUser.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        List<String> privileges = getPrivileges(roles);
        return getGrantedAuthorities(privileges);
    }

    private List<String> getPrivileges(List<Role> roles) {
        List<Privilege> privileges = new ArrayList<>();
        for (Role role : roles) {
            privileges.addAll(role.getPrivileges());
        }

        List<String> privilegeNames = new ArrayList<>();
        for (Privilege item : privileges) {
            privilegeNames.add(item.getName());
        }

        return privilegeNames;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privilegeNames) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String privilegeName : privilegeNames) {
            grantedAuthorities.add(new SimpleGrantedAuthority(privilegeName));
        }

        return grantedAuthorities;
    }
}
