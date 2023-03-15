    package org.Compnents.Renderer;

    import org.lwjgl.BufferUtils;

    import java.awt.*;
    import java.nio.ByteBuffer;
    import java.nio.IntBuffer;

    import static org.lwjgl.opengl.GL11.*;
    import static org.lwjgl.opengl.GL11.glTexParameteri;
    import static org.lwjgl.stb.STBImage.stbi_image_free;
    import static org.lwjgl.stb.STBImage.stbi_load;

    public class Texture {
        private String filepath;
        private int texID;
        public int width, height;

        public Texture(String fp) {
            this.filepath = fp;

            texID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texID);

            //Set Texture params
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            //stretching and shrinking image
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            IntBuffer channels = BufferUtils.createIntBuffer(1);
            //load image into bytebuffer using stbi
            ByteBuffer image = stbi_load(this.filepath, w, h, channels, 0);
            width = w.get(0);
            height = h.get(0);
            if(image != null){
                if(channels.get(0) == 3) {
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(0), h.get(0), 0,
                            GL_RGB, GL_UNSIGNED_BYTE, image);
                }
                else {
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get(0), h.get(0), 0,
                            GL_RGBA, GL_UNSIGNED_BYTE, image);
                }

            }
            else
            {
                System.err.println("ERROR IMAGE TEXTURE" + filepath);
            }
            //frees memory used in line 34
            assert image != null;
            stbi_image_free(image);
        }

        public void bind() {
            glBindTexture(GL_TEXTURE_2D, texID);
        }
        public void unbind() {
            glBindTexture(GL_TEXTURE_2D ,0);
        }
    }
