package com.assignment;

import io.javalin.Javalin;
import static com.assignment.Models.*;

public class App {
    public static void main(String[] args) {
        Javalin app = startServer(8080);
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
    }

    public static Javalin startServer(int port) {
        GraphService graph = new GraphService();
        Javalin app = Javalin.create().start(port);

        // 01 POST /people
        app.post("/people", ctx -> {
            try {
                CreatePersonRequest req = ctx.bodyAsClass(CreatePersonRequest.class);
                if (req.name() == null || req.name().trim().isEmpty()) {
                    ctx.status(400).json(new ErrorResponse("Name cannot be empty or blank"));
                    return;
                }
                Person p = graph.createPerson(req.name());
                ctx.status(201).json(p);
            } catch (Exception e) {
                ctx.status(400).json(new ErrorResponse("Invalid JSON or missing body field"));
            }
        });

        // 02 POST /knows
        app.post("/knows", ctx -> {
            try {
                KnowsRequest req = ctx.bodyAsClass(KnowsRequest.class);
                if (req.a() == null || req.b() == null || req.a().equals(req.b())) {
                    ctx.status(400).json(new ErrorResponse("Invalid connection fields"));
                    return;
                }
                if (!graph.exists(req.a())) {
                    ctx.status(404).json(new ErrorResponse("no person " + req.a()));
                    return;
                }
                if (!graph.exists(req.b())) {
                    ctx.status(404).json(new ErrorResponse("no person " + req.b()));
                    return;
                }
                graph.addConnection(req.a(), req.b());
                ctx.status(204);
            } catch (Exception e) {
                ctx.status(400).json(new ErrorResponse("Invalid JSON"));
            }
        });

        // 03 GET /people/{id}/contacts
        app.get("/people/{id}/contacts", ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                if (!graph.exists(id)) {
                    ctx.status(404).json(new ErrorResponse("no person " + id));
                    return;
                }
                ctx.status(200).json(graph.getContacts(id));
            } catch (NumberFormatException e) {
                ctx.status(400).json(new ErrorResponse("Malformed path parameter"));
            }
        });

        // 04 GET /path
        app.get("/path", ctx -> {
            String fromParam = ctx.queryParam("from");
            String toParam = ctx.queryParam("to");
            
            if (fromParam == null || toParam == null) {
                ctx.status(400).json(new ErrorResponse("Missing from or to parameters"));
                return;
            }
            
            try {
                int from = Integer.parseInt(fromParam);
                int to = Integer.parseInt(toParam);
                
                if (!graph.exists(from)) {
                    ctx.status(404).json(new ErrorResponse("no person " + from));
                    return;
                }
                if (!graph.exists(to)) {
                    ctx.status(404).json(new ErrorResponse("no person " + to));
                    return;
                }
                
                ctx.status(200).json(graph.findShortestPath(from, to));
            } catch (NumberFormatException e) {
                ctx.status(400).json(new ErrorResponse("Malformed from or to parameters"));
            }
        });

        return app;
    }
}
