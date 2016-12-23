<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
<xsl:template match="logMessages">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
		<fo:layout-master-set>
		<fo:simple-page-master page-height="297mm" page-width="210mm" margin-top="10mm" margin-left="20mm" margin-right="20mm" margin-bottom="10mm" master-name="PageMaster">
				<fo:region-before background-color="#EFEFEF" extent="40pt" />
				<fo:region-after background-color="#EFEFEF" extent="40pt" />
				<fo:region-start background-color="#EFAFEF" extent="40pt" reference-orientation="270" />
				<fo:region-end background-color="#EFAFEF" extent="40pt" reference-orientation="270" />
				<fo:region-body background-color="#EFFFAF" margin-top="56pt" margin-left="50pt" margin-right="50pt" margin-bottom="56pt" />
			</fo:simple-page-master>
		</fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          <fo:block font-size="10pt">   
              <xsl:apply-templates select="logMessage"/>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
     </fo:root>
</xsl:template>
<xsl:template match="logMessage">
	<fo:block>asfdsfadsfgasdfadsfsadfadsaffdsdf</fo:block>
  </xsl:template>
</xsl:stylesheet>