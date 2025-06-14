package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 Задача
Осуществите рефакторинг кода.
Реализуйте репозиторий с учётом того, что методы репозитория могут вызываться конкурентно, т. е. в разных потоках.
Как должен работать save:

Если от клиента приходит пост с id=0, значит, это создание нового поста. Вы сохраняете его в списке и присваиваете ему
новый id. Достаточно хранить счётчик с целым числом и увеличивать на 1 при создании каждого нового поста.

Если от клиента приходит пост с id !=0, значит, это сохранение (обновление) существующего поста. Вы ищете его в списке
по id и обновляете. Продумайте самостоятельно, что вы будете делать, если поста с таким id не оказалось: здесь могут быть
разные стратегии.
*/

public class PostRepository {
    private int postCount = 0;
    List<Post> postList = new CopyOnWriteArrayList<>();

    public List<Post> all() {
        return postList;
    }

    public Optional<Post> getById(long id) {

        return postList.stream().filter(post -> post.getId() == id).findFirst();
    }

    public Post save(Post post) {
        try {
            if (post.getId() == 0) {
                postCount++;
                post.setId(postCount);
                postList.add(post);
                return post;
            } else if (post.getId() != 0) {
                Optional<Post> optionalPost = getById(post.getId());
                if (optionalPost.isPresent()) {
                    Post post1 = optionalPost.get();
                    post1.setContent(post.getContent());
                    return post1;
                } else {
                    throw new NotFoundException("Post with ID " + post.getId() + " not found");
                }

            }
        } catch (NotFoundException nfe) {
            System.err.println("An unexpected error occurred: " + nfe.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void removeById(long id) {
        try {
            if (!getById(id).isPresent()) {
                throw new NotFoundException("Post with ID " + id + " not found");
            }
            postList.remove(id);
        } catch (NotFoundException nfe) {
            System.err.println("An unexpected error occurred: " + nfe.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
