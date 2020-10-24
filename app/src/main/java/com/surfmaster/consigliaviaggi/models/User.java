package com.surfmaster.consigliaviaggi.models;

public class User {
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String pwd;
    private String nickname;
    private Boolean showNickname;
    private String userRole;


    public User(Builder builder) {

        this.id=builder.id;
        this.nickname=builder.nickname;
        this.nome=builder.nome;
        this.cognome=builder.cognome;
        this.email=builder.email;
        this.pwd=builder.pwd;
    }

    public static class Builder {
        private int id;
        private String nickname;
        private String nome;
        private String cognome;
        private String email;
        private String pwd;

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }
        public Builder setName(String nome) {
            this.nome = nome;
            return this;
        }
        public Builder setSurname(String cognome) {
            this.cognome = cognome;
            return this;
        }
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }
        public Builder setPwd(String pwd) {
            this.pwd = pwd;
            return this;
        }
        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public User create() {
            return new User(this);
        }
    }



    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getShowNickname() {
        return showNickname;
    }

    public void setShowNickname(Boolean showNickname) {
        this.showNickname = showNickname;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
