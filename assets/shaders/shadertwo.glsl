#type vertex
#version 440
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexUV;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 model;

out vec4 fColor;
out vec2 fTexUV;

void main() {
    fColor = aColor;
    //fColor = vec4(1.0, 0.0, 0.0, 1.0);
    gl_Position = uProj * uView * model * vec4(aPos, 1.0);
    //pass uvs
    fTexUV = aTexUV;

}

#type fragment
#version 440
in vec4 fColor;

const int MAX_ITER = 18;
uniform vec3 iResolution;
uniform sampler2D TEX_SAMPLER;
//in vec4 fColor;
//out vec4 color;
in vec2 fTexUV;

out vec4 color;

uniform float fTime;

void main() {
    vec4 texColor = texture(TEX_SAMPLER, fTexUV);
    if(texColor.a < 0.1)
        discard;
    color = sin(fTime * 2) + fColor + texture(TEX_SAMPLER, fTexUV);
}

