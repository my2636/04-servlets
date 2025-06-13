package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  private PostController controller;

  @Override
  final public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  final protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals("GET")) {
        doGet(path, resp);
      }
      if (method.equals("POST")) {
        doPost(path, req, resp);
      }
      if (method.equals("DELETE")) {
        doDelete(path, resp);
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

    final protected void doGet(String path, HttpServletResponse resp) throws IOException {
        if (path.equals("/api/posts")) {
            controller.all(resp);
            return;
        }
        if (path.matches("/api/posts/\\d+")) {
            // easy way
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
            controller.getById(id, resp);
        }
    }

    final protected void doPost(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (path.equals("/api/posts")) {
            controller.save(req.getReader(), resp);
        }
    }

    final protected void doDelete(String path, HttpServletResponse resp) {
        if (path.matches("/api/posts/\\d+")) {
            // easy way
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
            controller.removeById(id, resp);
        }
    }
}

