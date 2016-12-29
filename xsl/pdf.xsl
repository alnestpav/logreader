<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:template match="logMessages">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
          <fo:layout-master-set>
            <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
              <fo:region-body/>
            </fo:simple-page-master>
          </fo:layout-master-set>
          <fo:page-sequence master-reference="simpleA4">
            <fo:flow flow-name="xsl-region-body">
                <fo:block-container text-align="center">
                      <fo:block font-size="18pt" font-weight="bold" space-after="5mm">logreader 1.0.1</fo:block>
                    <fo:block>
                        <fo:external-graphic content-type="content-type:image/gif" src="file:/C:/Users/alexander/IdeaProjects/logreader/xsl/siblion_logo.gif"/>
                    </fo:block>
                    <fo:block font-size="16pt" font-weight="bold" space-after="5mm">Siblion company</fo:block>
                    <fo:block font-size="14pt" space-after="5mm">Created by Alexander Nesterov</fo:block>
                </fo:block-container>
                <fo:block font-family="courier new" font-size="14pt">
                  <xsl:apply-templates select="logMessage"/>>
              </fo:block>
            </fo:flow>
          </fo:page-sequence>
         </fo:root>
    </xsl:template>

    <xsl:template name="string-replace-all">
        <xsl:param name="text"/>
        <xsl:param name="replace"/>
        <xsl:param name="by"/>
        <xsl:choose>
            <xsl:when test="contains($text, $replace)">
                <xsl:value-of select="substring-before($text,$replace)"/>
                <xsl:value-of select="$by" />
                <xsl:call-template name="string-replace-all">
                    <xsl:with-param name="text" select="substring-after($text,$replace)"/>
                    <xsl:with-param name="replace" select="$replace"/>
                    <xsl:with-param name="by" select="$by"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template match="logMessage">

        <xsl:variable name="message">
            <xsl:call-template name="string-replace-all">
                <xsl:with-param name="text" select="message" />
                <xsl:with-param name="replace" select="'&#09;'"/>
                <xsl:with-param name="by" select="'    '"/>
            </xsl:call-template>
        </xsl:variable>

        <fo:block>
            <fo:block>
                <fo:inline font-weight="bold">
                    <xsl:value-of select="date"/>
                </fo:inline>
            </fo:block>
            <fo:block space-after="1.0em" wrap-option="wrap" white-space="pre" white-space-collapse="false" white-space-treatment="preserve">
              <xsl:value-of select="$message"/>
            </fo:block>
        </fo:block>
      </xsl:template>
</xsl:stylesheet>