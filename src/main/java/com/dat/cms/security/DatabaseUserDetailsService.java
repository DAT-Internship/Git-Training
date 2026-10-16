package com.dat.cms.security;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service
public class DatabaseUserDetailsService implements UserDetailsService {
 private final JdbcTemplate jdbc;
 public DatabaseUserDetailsService(JdbcTemplate jdbc) { this.jdbc=jdbc; }
 @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  List<Row> rows=jdbc.query("SELECT user_id, username, password_hash, is_active FROM users WHERE username=?", (rs,n)->new Row(rs.getInt("user_id"),rs.getString("username"),rs.getString("password_hash"),rs.getBoolean("is_active")),username);
  if(rows.isEmpty()) throw new UsernameNotFoundException("User not found"); Row u=rows.get(0);
  if(!u.active()) throw new DisabledException("Account disabled");
  List<SimpleGrantedAuthority> roles=jdbc.query("SELECT r.role_code FROM user_roles ur JOIN roles r ON r.role_id=ur.role_id WHERE ur.user_id=?",(rs,n)->new SimpleGrantedAuthority("ROLE_"+rs.getString(1)),u.id());
  if(roles.isEmpty()) throw new UsernameNotFoundException("No role assigned");
  return User.withUsername(u.username()).password(u.hash()).authorities(roles).build();
 }
 private record Row(int id,String username,String hash,boolean active){}
}