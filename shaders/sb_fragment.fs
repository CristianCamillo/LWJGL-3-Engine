#version 330

in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 skyBoxLight;

void main()
{
    fragColor = vec4(skyBoxLight, 1) * texture(texture_sampler, outTexCoord);
}