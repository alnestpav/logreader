<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<w:wordDocument xmlns:w="http://schemas.microsoft.com/office/word/2003/wordml">
  <w:body>
  	<xsl:for-each select="/logMessages/logMessage">
	<w:p>
      <w:r>
        <w:t><xsl:value-of select="date"/></w:t>
      </w:r>
    </w:p>
		<w:p>
      <w:r>
        <w:t><xsl:value-of select="message"/></w:t>
      </w:r>
    </w:p>
		<w:p>
    </w:p>
	</xsl:for-each>
  </w:body>
</w:wordDocument>
</xsl:template>
</xsl:stylesheet>