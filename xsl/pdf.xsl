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
          <fo:block font-size="16pt" font-weight="bold" space-after="5mm">logreader 1.0.1</fo:block>
            <fo:block>
                <fo:external-graphic src="pdf.png" />
            </fo:block>
          <fo:block font-family="courier new" font-size="14pt">
              <xsl:apply-templates select="logMessage"/>>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
     </fo:root>
</xsl:template>
<xsl:template match="logMessage">
	<fo:block>
        <fo:block>
            <fo:inline font-weight="bold">
                <xsl:value-of select="date"/>
            </fo:inline>
        </fo:block>
        <fo:block space-after="1.0em" wrap-option="wrap" white-space="pre" white-space-collapse="false" white-space-treatment="preserve">
          <xsl:value-of select="message"/>
        </fo:block>
	</fo:block>
  </xsl:template>
</xsl:stylesheet>