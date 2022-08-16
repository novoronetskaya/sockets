module voronetskaya.figures_sockets {
    requires javafx.controls;
    requires javafx.fxml;


    opens voronetskaya.figures.sockets.client to javafx.fxml;
    exports voronetskaya.figures.sockets.client;
    exports voronetskaya.figures.sockets.server;
    opens voronetskaya.figures.sockets.server to javafx.fxml;
    exports voronetskaya.figures.sockets.objects;
    opens voronetskaya.figures.sockets.objects to javafx.fxml;
}