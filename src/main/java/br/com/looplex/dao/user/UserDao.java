package br.com.looplex.dao.user;

import br.com.looplex.dao.IDao;
import br.com.looplex.models.ElasticsearchUser;

public abstract class UserDao
    implements IDao<ElasticsearchUser,String>
{

    public void enableUser(String id) {};
    public void disableUser(String id) {};

}
