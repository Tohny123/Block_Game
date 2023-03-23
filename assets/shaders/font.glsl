#type vertex
#version 440
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexUV;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 model;
uniform vec2 UV;

out vec4 fColor;
out vec2 fTexUV;

void main() {
    fColor = aColor;
    //fColor = vec4(1.0, 0.0, 0.0, 1.0);
    gl_Position = uProj * uView * model * vec4(aPos, 1.0);
    //pass uvs
    fTexUV = UV;

}

#type fragment
#version 440
in vec4 fColor;
const int MAX_ITER = 18;
uniform vec3 iResolution;
uniform vec4 spriteCol;
uniform sampler2D TEX_SAMPLER;
//in vec4 fColor;
//out vec4 color;
in vec2 fTexUV;

out vec4 color;

uniform float fTime;

void main() {

    //color = sin(fTime * 2) + fColor + texture(TEX_SAMPLER, fTexUV);
    //color = texColor * spriteCol;
    //color = spriteCol * texture(TEX_SAMPLER, fTexUV);
    vec4 texture = texture(TEX_SAMPLER, fTexUV);
    color = vec4(spriteCol.x * texture.x, spriteCol.y * texture.y, spriteCol.z * texture.z, texture.w);
}

