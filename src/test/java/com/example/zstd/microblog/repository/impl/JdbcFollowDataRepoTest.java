package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.model.FollowData;
import com.example.zstd.microblog.model.User;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;

import static com.example.zstd.microblog.model.UserBuilder.anUser;
import static org.junit.Assert.assertEquals;

public class JdbcFollowDataRepoTest extends JdbcRepoTestBase {

    private static final Logger LOG = Logger.getLogger(BasicJdbcRepo.class.getName());

    private JdbcFollowDataRepo followDataRepo;

    @Override
    protected void setUpRepos() {
        followDataRepo = new JdbcFollowDataRepo();
        userRepo = new JdbcUserRepo();
    }

    @Test
    public void testCreateListDeleteByName() throws Exception {
        LOG.info("testCreate");
        User follower = givenUserExists(anUser().withUsername("one").withNickname("one").withPassword("one")),
            following = givenUserExists(anUser().withUsername("two").withNickname("two").withPassword("two"));

        thenFollowDataWithSize(follower.getUsername(),following.getUsername(),0);

        whenSavingFollowData(follower, following);

        thenFollowDataWithSize(follower.getUsername(),following.getUsername(),1);

        followDataRepo.delete(follower.getUsername(),following.getUsername());

        thenFollowDataWithSize(follower.getUsername(),following.getUsername(),0);

    }

    @Test
    public void testCreateListDeleteById() throws Exception {
        LOG.info("testCreate");
        User follower = givenUserExists(anUser().withUsername("uno").withNickname("uno").withPassword("uno")),
                following = givenUserExists(anUser().withUsername("dos").withNickname("dos").withPassword("dos"));

        thenFollowDataWithSize(follower.getUsername(),following.getUsername(),0);

        FollowData saved = whenSavingFollowData(follower, following);

        thenFollowDataWithSize(follower.getUsername(),following.getUsername(),1);

        followDataRepo.deleteById("follow_data",FollowData.DB_FIELD_ID,saved.getId());

        thenFollowDataWithSize(follower.getUsername(),following.getUsername(),0);

    }

    private FollowData whenSavingFollowData(User follower, User following) {
        FollowData toSave = new FollowData(follower.getUsername(),following.getUsername());
        FollowData saved = followDataRepo.save(toSave);
        LOG.info("saved: " + saved);
        assertEquals(saved.getFollower(),follower.getUsername());
        assertEquals(saved.getFollowing(),following.getUsername());
        return saved;
    }

    private void thenFollowDataWithSize(String follower,String following,int count) {
        List<FollowData> list = followDataRepo.findByField(FollowData.DB_FIELD_FOLLOWER,follower);
        assertEquals(list.size(), count);
        list = followDataRepo.findByField(FollowData.DB_FIELD_FOLLOWING,following);
        assertEquals(list.size(), count);
    }
}